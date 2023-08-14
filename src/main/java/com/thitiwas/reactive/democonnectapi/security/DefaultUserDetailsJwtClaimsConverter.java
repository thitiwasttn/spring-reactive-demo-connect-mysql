/*
 * Copyright 2019-Current jittagornp.me
 */
package com.thitiwas.reactive.democonnectapi.security;

import java.util.Map;

/**
 * @author jitta
 */
public interface DefaultUserDetailsJwtClaimsConverter {

    DefaultUserDetails convert(final Map<String, Object> claims);

    Map<String, Object> convert(final DefaultUserDetails userDetails);

}
