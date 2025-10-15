ALTER TABLE danuri_prod.space
    ADD allow_overlap BIT(1) NULL DEFAULT FALSE;

ALTER TABLE danuri_prod.usage_history
    ADD notified_at datetime NULL;