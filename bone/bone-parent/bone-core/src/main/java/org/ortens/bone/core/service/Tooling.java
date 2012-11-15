package org.ortens.bone.core.service;

import java.lang.annotation.*;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Tooling {

	String name() default "";
	
	String responsability() ;
	
}
