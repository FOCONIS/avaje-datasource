package org.avaje.datasource.pool;


import org.testng.annotations.Test;

public class TestDataSourceMax {

  @Test
  public void test() {

//    boolean skipThisTest = true;
//
//    if (skipThisTest) {
//      return;
//    }
//
//    String name = "mysql";
//
//    Properties properties = PropertyMap.defaultProperties();
//
//    DataSourceConfig dsConfig = new DataSourceConfig();
//    dsConfig.loadSettings(properties, name);
//    dsConfig.setMinConnections(2);
//    dsConfig.setMaxConnections(25);
//    dsConfig.setWaitTimeoutMillis(30000);
//    dsConfig.setCaptureStackTrace(true);
//
//    DDataSourcePool pool = new DDataSourcePool(name, dsConfig, null);
//
//
//    //pool.checkDataSource();
//
////    if (true) {
////      pool.shutdown(false);
////      return;
////    }
//
//    DefaultBackgroundExecutor bg = new DefaultBackgroundExecutor(1, 1, 2, 180, 30, "testDs");
//
//    try {
//      for (int i = 0; i < 12; i++) {
//        // Thread.sleep(10*i);
//        bg.execute(new ConnRunner(pool, 4000, i));
//      }
//
//      Thread.sleep(10000);
//      pool.getStatistics(true);
//
//      Thread.sleep(30000);
//
//      Status status = pool.getStatus(false);
//      System.out.println(status);
//
//      pool.shutdown(false);
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//  }
//
//  private static class ConnRunner implements Runnable {
//
//    final DataSourcePool pool;
//    final long sleepMillis;
//    final int position;
//
//    ConnRunner(DataSourcePool pool, long sleepMillis, int position) {
//      this.pool = pool;
//      this.sleepMillis = sleepMillis;
//      this.position = position;
//    }
//
//    private void waitSomeTime(long count) {
//      try {
//      Thread.sleep(sleepMillis);
//      } catch (InterruptedException e){
//        throw new RuntimeException(e);
//      }
//    }
//
//    public void run() {
//      Connection connection = null;
//      PreparedStatement pstmt = null;
//      ResultSet rset = null;
//      long count = -1;
//      try {
//        connection = pool.getConnection();
//        pstmt = connection.prepareStatement("select count(*) from o_customer");
//        rset = pstmt.executeQuery();
//
//        while (rset.next()) {
//          // do nothing actually
//          count = rset.getLong(1);
//        }
//
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      } finally {
//        if (rset != null) {
//          try {
//            rset.close();
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//          if (pstmt != null) {
//            try {
//              pstmt.close();
//            } catch (Exception e) {
//              e.printStackTrace();
//            }
//          }
//          if (connection != null) {
//            try {
//              connection.close();
//            } catch (Exception e) {
//              e.printStackTrace();
//            }
//          }
//        }
//        waitSomeTime(count);
//      }
//    }
  }
}
