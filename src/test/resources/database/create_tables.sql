CREATE TABLE IF NOT EXISTS profiles
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(50)  NOT NULL,
    surname           VARCHAR(50)  NOT NULL,
    job_title         VARCHAR(100),
    phone             VARCHAR(15),
    email             VARCHAR(100) NOT NULL,
    address           VARCHAR(200),
    avatar_image_name VARCHAR(100),
    is_public         BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS interests
(
    profile_id     UUID        NOT NULL,
    name           VARCHAR(30) NOT NULL,
    interest_order INT         NOT NULL,
    CONSTRAINT fk_interest_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS links
(
    profile_id UUID         NOT NULL,
    name       VARCHAR(30)  NOT NULL,
    url        VARCHAR(200) NOT NULL,
    link_order INT          NOT NULL,
    CONSTRAINT fk_links_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);