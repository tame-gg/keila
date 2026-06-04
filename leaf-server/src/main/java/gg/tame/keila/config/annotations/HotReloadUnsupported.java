package gg.tame.keila.config.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a config module (or field) whose values are applied only at startup. {@code /keila reload} still re-reads
 * YAML but runtime hooks may require a full restart.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.FIELD})
public @interface HotReloadUnsupported {
}
