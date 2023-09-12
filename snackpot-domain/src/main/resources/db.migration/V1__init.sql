create table if not exists Exercise
(
    id               bigint auto_increment           primary key,
    calories         int                             null,
    timeSpent        int                             null,
    createdAt        datetime(6)                     null,
    updatedAt        datetime(6)                     null,
    effect           varchar(255)                    null,
    level            varchar(255)                    null,
    thumbnail        varchar(255)                    null,
    title            varchar(255)                    null,
    videoId          varchar(255)                    null,
    name             varchar(255)                    null,
    status           enum ('ACTIVE', 'INACTIVE')     null,
    videoLink        varchar(255)                    null
    );

create table if not exists Exgroup
(
    checkIntervalTime    int                         null,
    checkMaxNum          int                         null,
    endDate              date                        null,
    endTime              time(6)                     null,
    existDays            int                         null,
    goalRelayNum         int                         null,
    isGoalAchieved       bit                         null,
    maxMemberNum         int                         null,
    startDate            date                        null,
    startTime            time(6)                     null,
    createdAt            datetime(6)                 null,
    currentDoingMemberId bigint                      null,
    id                   bigint auto_increment       primary key,
    updatedAt            datetime(6)                 null,
    code                 varchar(255)                null,
    color                varchar(255)                null,
    description          varchar(255)                null,
    emozi                varchar(255)                null,
    name                 varchar(255)                null,
    penalty              varchar(255)                null,
    status               enum ('ACTIVE', 'INACTIVE') null
    );

create table if not exists JoinList
(
    executedMissionCount int                         null,
    outCount             int                         null,
    createdAt            datetime(6)                 null,
    groupId              bigint                      null,
    id                   bigint auto_increment
    primary key,
    memberId             bigint                      null,
    updatedAt            datetime(6)                 null,
    joinType             enum ('HOST', 'MEMBER')     null,
    status               enum ('ACTIVE', 'INACTIVE') null,
    constraint FK1gw52v3ko0ae8gfaxdyc5cl4n
    foreign key (groupId) references Exgroup (id),
    constraint FKnd6hkqodhq2s2xvjxqqriy5ld
    foreign key (memberId) references Member (id)
    );

create table if not exists Member
(
    birthYear    int                         null,
    createdAt    datetime(6)                 null,
    id           bigint auto_increment
    primary key,
    updatedAt    datetime(6)                 null,
    email        varchar(255)                null,
    fcmToken     varchar(255)                null,
    gender       enum ('FEMALE', 'MALE')     null,
    name         varchar(255)                null,
    nickname     varchar(255)                null,
    password     varchar(255)                null,
    profileImage varchar(255)                null,
    role         enum ('GUEST', 'USER')      null,
    socialId     varchar(255)                null,
    socialType   enum ('KAKAO')              null,
    status       enum ('ACTIVE', 'INACTIVE') null
    );


create table if not exists Mission
(
    alarmCount int         null,
    calory     int         null,
    createdAt  datetime(6) null,
    endAt      datetime(6) null,
    exerciseId bigint      null,
    groupId    bigint      null,
    id         bigint auto_increment
    primary key,
    memberId   bigint      null,
    startAt    datetime(6) null,
    updatedAt  datetime(6) null,
    constraint FK7pqjgg3edg84l69cpeidu2xtl
    foreign key (exerciseId) references Exercise (id),
    constraint FK8dk26v5pb3mi96n3sa73q6cjx
    foreign key (memberId) references Member (id),
    constraint FKnb6vflbo3eopa3rmd1vsard7a
    foreign key (groupId) references Exgroup (id)
    );




