CREATE OR REPLACE FUNCTION create_info_after_user_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_profile (user_id)
    VALUES (NEW.user_id);

	INSERT INTO user_destination (user_id)
	VALUES(NEW.user_id);

	INSERT INTO user_website (user_id)
	VALUES (NEW.user_id);
	

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_create_info_after_user
AFTER INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION create_info_after_user_insert();

