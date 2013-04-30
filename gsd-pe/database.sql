CREATE TABLE `policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fromTime` time NOT NULL,
  `toTime` time NOT NULL,
  `active` binary(1) NOT NULL,
  `policy` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=UTF8

