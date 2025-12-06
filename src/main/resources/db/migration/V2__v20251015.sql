ALTER TABLE space
    ADD allow_overlap BIT(1) NULL DEFAULT FALSE;

ALTER TABLE usage_history
    ADD notified_at datetime NULL;