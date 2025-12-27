create extension if not exists pgcrypto;

create table if not exists users (
    id uuid primary key default gen_random_uuid(),
    handle varchar(100) not null unique,
    email varchar(255) not null unique,
    trust_level varchar(16) not null,
    read_time_minutes bigint not null default 0,
    posts int not null default 0,
    likes_given int not null default 0,
    likes_received int not null default 0,
    flags_received int not null default 0,
    days_visited int not null default 0,
    created_at timestamptz not null
);

create table if not exists topics (
    id uuid primary key default gen_random_uuid(),
    title varchar(255) not null,
    author_id uuid not null,
    category varchar(100) not null,
    reply_count int not null default 0,
    last_post_at timestamptz
);

create table if not exists posts (
    id uuid primary key default gen_random_uuid(),
    topic_id uuid not null,
    author_id uuid not null,
    state varchar(20) not null,
    content text not null,
    like_count int not null default 0,
    flag_weight int not null default 0
);

create table if not exists flags (
    id uuid primary key default gen_random_uuid(),
    post_id uuid not null,
    user_id uuid not null,
    reason varchar(255) not null,
    weight int not null,
    created_at timestamptz not null
);

create table if not exists audit_logs (
    id uuid primary key default gen_random_uuid(),
    actor_id uuid not null,
    action varchar(100) not null,
    resource_ref varchar(255) not null,
    details text not null,
    at timestamptz not null
);
