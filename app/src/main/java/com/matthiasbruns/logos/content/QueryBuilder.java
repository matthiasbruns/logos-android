package com.matthiasbruns.logos.content;

/**
 * Created by Matthias Bruns on 17/02/15.
 * Used to provide a default interface to build content provider queries
 */
public interface QueryBuilder {

    String findById(final long id);
}
