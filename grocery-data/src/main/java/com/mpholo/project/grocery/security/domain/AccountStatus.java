package com.mpholo.project.grocery.security.domain;

/**
 * Lifecycle of a self-registered account:
 * PENDING  - signed up, awaiting superadmin approval
 * APPROVED - approved by a superadmin; activation link emailed, not yet activated
 * ACTIVE   - user followed the link and set a username + password; can log in
 * REJECTED - superadmin declined the signup
 * BLOCKED  - access revoked
 *
 * Existing (pre-feature) users have a null status and are gated only by the
 * {@code enabled} flag, so they keep working unchanged.
 */
public enum AccountStatus {
    PENDING,
    APPROVED,
    ACTIVE,
    REJECTED,
    BLOCKED
}
