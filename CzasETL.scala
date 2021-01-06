package com.example.bigdata

import java.util.Calendar

import org.apache.spark.sql._
import org.apache.spark.sql.types.{LongType, StructField, StructType}

object CzasETL {

  def main(args: Array[String]) {

    val spark = SparkSession.builder()
      .appName("CzasETL")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()

    val scotlandMainDS = spark.read.format("org.apache.spark.csv")
      .option("header", true)
      .option("inferSchema", true)
      .csv(args(0) + "/mainDataScotland.csv")

    val northEnglandMainDS = spark.read.format("org.apache.spark.csv")
      .option("header", true)
      .option("inferSchema", true)
      .csv(args(0) + "/mainDataNorthEngland.csv")

    val southEnglandMainDS = spark.read.format("org.apache.spark.csv")
      .option("header", true)
      .option("inferSchema", true)
      .csv(args(0) + "/mainDataSouthEngland.csv")

    val time = scotlandMainDS
      .union(northEnglandMainDS)
      .union(southEnglandMainDS)
      .select("count_date", "hour")
      .distinct()

    val timeWithIndex = spark.sqlContext.createDataFrame(
      time.rdd.zipWithIndex.map {
        case (row, index) => {
          val calendar = Calendar.getInstance

          calendar.setTime(row.getTimestamp(0))

          val month = calendar.get(Calendar.MONTH).toLong

          Row.fromSeq(row.toSeq :+
            (index + 1) :+
            calendar.get(Calendar.YEAR).toLong :+
            month :+
            (month / 3 + 1) :+
            calendar.get(Calendar.DAY_OF_WEEK).toLong
          )
        }
      },
      StructType(time.schema.fields :+
        StructField("id_czas", LongType, false) :+
        StructField("rok", LongType, false) :+
        StructField("miesiac", LongType, false) :+
        StructField("kwartal", LongType, false) :+
        StructField("dzien_tygodnia", LongType, false)
      )
    )

    timeWithIndex
      .withColumnRenamed("count_date", "data")
      .withColumnRenamed("hour", "godzina")
      .select("id_czas", "data", "rok", "miesiac", "godzina", "kwartal", "dzien_tygodnia")
      .write
      .insertInto("w_czas")

    println("Załadowano dane do tabeli wymiaru 'w_czas'")
  }
}