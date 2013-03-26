CREATE  TABLE `webaholic_gsd`.`policy` (
  `id` BIGINT NOT NULL ,
  `fromTime` TIME NOT NULL ,
  `toTime` TIME NOT NULL ,
  `active` BINARY NOT NULL ,
  `policy` TEXT NOT NULL ,
  PRIMARY KEY (`id`) );

  ALTER TABLE `webaholic_gsd`.`policy` CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT  ;
  