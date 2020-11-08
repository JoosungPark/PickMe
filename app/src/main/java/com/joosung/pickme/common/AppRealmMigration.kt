package com.joosung.pickme.common

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import java.util.*

class AppRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        var version = oldVersion

        if (version == 0L) {
            schema.get(AppRealmMedia::class.java.simpleName) ?: kotlin.run {
                schema.create(AppRealmMedia::class.java.simpleName)
                    .addField(AppRealmMedia.ID, String::class.java, FieldAttribute.REQUIRED)
                    .addPrimaryKey(AppRealmMedia.ID)
                    .addField(AppRealmMedia.kVideoThumbnail, String::class.java)
                    .setNullable(AppRealmMedia.kVideoThumbnail, true)
                    .addField(AppRealmMedia.kImageUrl, String::class.java)
                    .setNullable(AppRealmMedia.kImageUrl, true)
                    .addField(AppRealmMedia.kDateTime, Date::class.java, FieldAttribute.REQUIRED)
                    .addField(AppRealmMedia.kWidth, Int::class.java)
                    .setNullable(AppRealmMedia.kWidth, true)
                    .addField(AppRealmMedia.kHeight, Int::class.java)
                    .setNullable(AppRealmMedia.kHeight, true)
                    .addField(AppRealmMedia.kStarred, Boolean::class.java)
                    .setNullable(AppRealmMedia.kStarred, true)

            }
            version += 1
        }
    }
}