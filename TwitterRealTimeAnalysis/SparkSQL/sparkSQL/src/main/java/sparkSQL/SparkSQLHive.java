package sparkSQL;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

public class SparkSQLHive {
	public static void main(String[] args){
		SparkSession ss = SparkSession.builder().appName("SparkSQLHive").master("local[2]")
				.config("hive.metastore.uris", "thrift://quickstart.cloudera:9083") .enableHiveSupport() .getOrCreate();
		SQLContext sqlContext = SQLContext.getOrCreate(ss.sparkContext());
		sqlContext.sql("drop table if exists hdfsHive");
		Dataset<Row> jsonTest = sqlContext.read().json("hdfs://quickstart.cloudera:8020/user/cloudera/input/");
		jsonTest.createOrReplaceTempView("jsonTest"); jsonTest.createOrReplaceTempView("jsonTest"); 
		jsonTest.show();
		sqlContext.sql("create table " + "hdfsHive" + " using hive as select * from "+ "jsonTest");
		

	}
}


 
