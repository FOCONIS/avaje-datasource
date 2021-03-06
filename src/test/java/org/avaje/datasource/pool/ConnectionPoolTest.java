package org.avaje.datasource.pool;

import org.avaje.datasource.DataSourceConfig;
import org.avaje.datasource.PoolStatistics;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConnectionPoolTest {

  private ConnectionPool pool;

  ConnectionPoolTest() {
    pool = createPool();
  }

  private ConnectionPool createPool() {

    DataSourceConfig config = new DataSourceConfig();
    config.setDriver("org.h2.Driver");
    config.setUrl("jdbc:h2:mem:tests");
    config.setUsername("sa");
    config.setPassword("");
    config.setMinConnections(2);
    config.setMaxConnections(4);

    return new ConnectionPool("test", config);
  }

  @AfterClass
  public void after() {
    pool.shutdown(false);
  }

  @Test
  public void getConnection_expect_poolGrowsAboveMin() throws SQLException {

    Connection con1 = pool.getConnection();
    Connection con2 = pool.getConnection();

    assertThat(pool.getStatus(false).getBusy()).isEqualTo(2);
    assertThat(pool.getStatus(false).getFree()).isEqualTo(0);

    Connection con3 = pool.getConnection();
    assertThat(pool.getStatus(false).getBusy()).isEqualTo(3);
    assertThat(pool.getStatus(false).getFree()).isEqualTo(0);

    con2.close();
    assertThat(pool.getStatus(false).getBusy()).isEqualTo(2);
    assertThat(pool.getStatus(false).getFree()).isEqualTo(1);

    con3.close();
    assertThat(pool.getStatus(false).getBusy()).isEqualTo(1);
    assertThat(pool.getStatus(false).getFree()).isEqualTo(2);

    con1.close();
    assertThat(pool.getStatus(false).getBusy()).isEqualTo(0);
    assertThat(pool.getStatus(false).getFree()).isEqualTo(3);
  }

  @Test(dependsOnMethods = "getConnection_expect_poolGrowsAboveMin")
  public void getConnection_getStatistics() throws SQLException, InterruptedException {

    pool.getStatistics(true);

    Connection con1 = pool.getConnection();
    Connection con2 = pool.getConnection();

    Thread.sleep(100);
    con1.close();
    con2.close();

    PoolStatistics statistics = pool.getStatistics(false);

    assertThat(statistics.getCount()).isEqualTo(2);
    assertThat(statistics.getTotalMicros()).isGreaterThan(190000);
    assertThat(statistics.getHwmMicros()).isGreaterThan(90000);
    assertThat(statistics.getAvgMicros()).isGreaterThan(90000);
  }

  @Test
  public void getConnection_explicitUserPassword() throws SQLException {

    Connection connection = pool.getConnection("sa", "");
    PreparedStatement statement = connection.prepareStatement("create user testing password '123'");
    statement.execute();
    statement.close();
    connection.close();

    Connection another = pool.getConnection("testing", "123");
    another.close();
  }

  @Test
  public void unwrapConnection() throws SQLException {

    Connection connection = pool.getConnection();
    Connection underlying = connection.unwrap(Connection.class);

    assertThat(underlying).isInstanceOf(org.h2.jdbc.JdbcConnection.class);
    connection.close();
  }

  @Test
  public void getDelegate() throws SQLException {

    Connection connection = pool.getConnection();
    PooledConnection pc = (PooledConnection)connection;
    Connection underlying = pc.getDelegate();

    assertThat(underlying).isInstanceOf(org.h2.jdbc.JdbcConnection.class);
    connection.close();
  }
  
  @Test
  public void testSchemaSwitch() throws SQLException {
    Connection conn = pool.getConnection();
    Statement stmt = conn.createStatement();
    stmt.executeUpdate("CREATE SCHEMA TENANT_1");
    stmt.executeUpdate("CREATE SCHEMA TENANT_2");
    stmt.executeUpdate("CREATE TABLE TENANT_1.LOCAL_MODEL (id integer)");
    stmt.executeUpdate("CREATE TABLE TENANT_2.LOCAL_MODEL (id integer)");
    stmt.close();
    
    conn.setSchema("TENANT_1");
    PreparedStatement ps1 = conn.prepareStatement("SELECT * from local_model");
    ps1.close();
    PreparedStatement ps2 = conn.prepareStatement("SELECT * from local_model");
    ps2.close();

    conn.setSchema("TENANT_2");
    PreparedStatement ps3 = conn.prepareStatement("SELECT * from local_model");
    ps3.close();


    assertThat(ps1).isSameAs(ps2);  // test if pstmtCache is working
    assertThat(ps1).isNotSameAs(ps3); // test if datasource recognize schema change
  }
}