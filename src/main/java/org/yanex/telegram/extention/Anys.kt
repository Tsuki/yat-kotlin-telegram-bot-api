package org.yanex.telegram.extention

/**Created by Alicex on 4/2/17.**/
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

inline fun <T> T?.ifPresent(lambda: (T) -> Unit) {
    if (this != null) {
        lambda(this)
    }
}

inline fun <T> T?.ifAbsent(lambda: () -> Unit) {
    if (this == null) {
        lambda()
    }
}

fun Any?.toStrings(toStringSytle: ToStringStyle = ToStringStyle.SIMPLE_STYLE): String {
    ifPresent {
        return ReflectionToStringBuilder.toString(this, toStringSytle)
    }
    return "null"
}