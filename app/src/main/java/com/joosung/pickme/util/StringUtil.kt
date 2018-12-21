package com.joosung.pickme.util

class StringUtil {
    companion object {
        fun concat(vararg objects: Any): String {
            return concatenates(*objects)
        }

        fun concat(widthBlank: Boolean, vararg objects: Any): String {
            return concatenates(widthBlank, *objects)
        }

        private fun concatenates(vararg objects: Any): String {
            return concatenates(false, *objects)
        }

        private fun concatenates(widthBlank: Boolean, vararg objects: Any): String {
            if (objects.isEmpty()) {
                return ""
            }

            val stringBuilder = StringBuilder()
            objects.forEach {
                stringBuilder.append(it)
                if (widthBlank) stringBuilder.append(" ")
            }

            return stringBuilder.toString()
        }

        /**
         * Check if any {charSequence} contains Korean string.
         */
        fun hasKorean(charSequence: CharSequence): Boolean {
            return charSequence.toString().toCharArray().any {
                Character.UnicodeBlock.of(it) === Character.UnicodeBlock.HANGUL_JAMO
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.HANGUL_SYLLABLES
            }
        }

        fun hasJapanese(charSequence: CharSequence): Boolean {
            return charSequence.toString().toCharArray().any {
                Character.UnicodeBlock.of(it) === Character.UnicodeBlock.HIRAGANA
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.KATAKANA
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                        || Character.UnicodeBlock.of(it) === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            }
        }
    }
}