spark.sql("DROP TABLE IF EXISTS `w_pogoda`")
spark.sql("DROP TABLE IF EXISTS `w_przestrzen`")
spark.sql("DROP TABLE IF EXISTS `w_kategoria_drogi`")
spark.sql("DROP TABLE IF EXISTS `w_czas`")
spark.sql("DROP TABLE IF EXISTS `w_typ_pojazdu`")
spark.sql("DROP TABLE IF EXISTS `f_fakty`")


spark.sql(
    """CREATE TABLE `w_pogoda` (
        `id_pogody` int,
        `opis_pogody` string)
    ROW FORMAT SERDE
        'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
        'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
        'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")

spark.sql(
  """CREATE TABLE `w_przestrzen` (
      `id_przestrzen` int,
      `nazwa_regionu` string,
      `nazwa_obszaru_adm` string,
      `kod_obszaru_adm` string,
      `nazwa_drogi` string)
    ROW FORMAT SERDE
      'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")

spark.sql(
  """CREATE TABLE `w_kategoria_drogi` (
      `id_kat_drogi` int,
      `kategoria_drogi` string,
      `typ_drogi` string)
    ROW FORMAT SERDE
      'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")

spark.sql(
  """CREATE TABLE `w_czas` (
      `id_czas` int,
      `data` date,
      `rok` int,
      `miesiac` int,
      `godzina` int,
      `kwartal` int,
      `dzien_tygodnia` int)
    ROW FORMAT SERDE
      'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")

spark.sql(
  """CREATE TABLE `w_typ_pojazdu` (
      `id_typ` int,
      `typ_pojazdu` string,
      `kategoria` string,
      `silnikowy` boolean)
    ROW FORMAT SERDE
      'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")

spark.sql(
  """CREATE TABLE `f_fakty` (
      `id_pogody` int,
      `id_przestrzen` int,
      `id_kat_drogi` int,
      `id_czas` int,
      `id_typ_pojazdu` int,
      `liczba_pojazdow` int)
    ROW FORMAT SERDE
      'org.apache.hadoop.hive.ql.io.orc.OrcSerde'
    STORED AS INPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat'
    OUTPUTFORMAT
      'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat'""")