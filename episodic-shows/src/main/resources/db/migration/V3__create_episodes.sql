create table episodes (
  id bigint not null auto_increment primary key,
  show_id bigint not null,
  episode_number int(11) not null,
  season_number int(11) not null,
  foreign key (show_id) references shows(id)
);