CREATE TABLE `policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fromTime` time NOT NULL,
  `toTime` time NOT NULL,
  `active` binary(1) NOT NULL,
  `policy` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=70 DEFAULT CHARSET=latin1$$

