SET search_path TO skylinedb;

DO language plpgsql $$
BEGIN
	RAISE NOTICE 'Updating Skyline App to version 1.0.0';
END
$$;

INSERT INTO VERSION_AUDIT (version, init_installation, finish_installation)
VALUES ('1.0.0', now(), null);

--

--

UPDATE VERSION_AUDIT SET finish_installation = NOW() WHERE version = '1.0.0';

DO language plpgsql $$
BEGIN
	RAISE NOTICE 'Skyline App version 1.0.0 UPDATED';
END
$$;