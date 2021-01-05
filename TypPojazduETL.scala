package com.example.bigdata

import org.apache.spark.sql.SparkSession
import scala.collection.mutable

case class Typ_Pojazdu(id_typ: Int, typ_pojadu: String, kategoria: String, silnikowy: Boolean)

object TypPojazduETL {

  def main(args: Array[String]) {

    val categories = Map(
      "all_motor_vehicles" -> List("All Motor Vehicles", "All_MV"),
      "two_wheeled_motor_vehicles" -> List("Two-wheeled motor vehicles", "2WMV"),
      "cars_and_taxis" -> List("Cars and Taxis", "Car"),
      "lgvs" -> List("Light Goods Vans", "LGV"),
      "buses_and_coaches" -> List("Buses and coaches", "BC"),
      "all_hgvs" -> List("Heavy Goods Vehicle total", "HGV"),
      "hgvs_2_rigid_axle" -> List("2-rigid axle Heavy Goods Vehicle", "HGV"),
      "hgvs_3_rigid_axle" -> List("3-rigid axle Heavy Goods Vehicle", "HGV"),
      "hgvs_4_or_more_rigid_axle" -> List("4 or more rigid axle Heavy Goods Vehicle", "HGV"),
      "hgvs_3_or_4_articulated_axle" -> List("3 and 4-articulated axle Heavy Goods Vehicle", "HGV"),
      "hgvs_5_articulated_axle" -> List("5-articulated axle Heavy Goods Vehicle", "HGV"),
      "hgvs_6_articulated_axle" -> List("6 or more articulated axle Heavy Goods Vehicle", "HGV"),
      "pedal_cycles" -> List("Pedal Cycles", "PC")
    )

    val spark = SparkSession.builder()
      .appName("TypPojazduETL")
      .master("local")
      .enableHiveSupport()
      .getOrCreate();

    import spark.implicits._
    val scotlandMainDS = spark.read.format("org.apache.spark.csv")
      .option("header", false)
      .option("inferSchema", true)
      .csv(args(0) + "/mainDataScotland.csv")

    val vehicles = mutable.MutableList[Typ_Pojazdu]()
    var id_pojazdu: Int = 1
    val header = scotlandMainDS.first().toSeq.toList.takeRight(13)

    header.foreach(p => {
      vehicles += Typ_Pojazdu(id_pojazdu,  categories(p.toString)(0), categories(p.toString)(1), if("pedal_cycles".equals(p)) false else true)
      id_pojazdu += 1
    })

    val typPojazduDS = vehicles.toDS()

    typPojazduDS.write.insertInto("w_typ_pojazdu")

    println("Załadowano dane do tabeli wymiaru 'w_typ_pojazu'")
  }
}
