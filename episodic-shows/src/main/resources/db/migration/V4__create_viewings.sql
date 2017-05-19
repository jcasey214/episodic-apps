create table viewings (
  id bigint not null auto_increment primary key,
  user_id bigint not null,
  show_id bigint not null,
  episode_id bigint not null,
  updated_at datetime not null,
  timecode int(11) not null,
  foreign key (show_id) references shows(id),
  foreign key (user_id) references users(id),
  foreign key (episode_id) references episodes(id)
);