DROP TABLE IF EXISTS `matc93`.`movie`;
CREATE TABLE  `matc93`.`movie` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL DEFAULT '-',
  `country` varchar(150) NOT NULL DEFAULT '-',
  `abstract` varchar(5000) NOT NULL DEFAULT '-',
  `budget` varchar(45) NOT NULL DEFAULT '-',
  `releaseDate` varchar(45) NOT NULL DEFAULT '-',
  `runtime` varchar(45) NOT NULL DEFAULT '-',
  `director` varchar(500) NOT NULL DEFAULT '-',
  `producer` varchar(500) NOT NULL DEFAULT '-',
  `starring` varchar(700) NOT NULL DEFAULT '-',
  `alternateTitle` varchar(300) NOT NULL DEFAULT '-',
  `genre` varchar(500) NOT NULL DEFAULT '-',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;