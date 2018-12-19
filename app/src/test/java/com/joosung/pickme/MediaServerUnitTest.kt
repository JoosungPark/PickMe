package com.joosung.pickme

import com.google.gson.GsonBuilder
import com.joosung.pickme.http.api.GetImageResponse
import com.joosung.pickme.http.api.GetVideoResponse
import com.joosung.pickme.http.model.DateDeserializer
import com.joosung.pickme.http.model.MediaUrl
import com.joosung.pickme.http.model.OptionalTypeAdapter
import com.joosung.pickme.http.model.SharedMedia
import com.joosung.pickme.ui.home.HomeViewModel
import com.joosung.pickme.ui.home.MediaServerInterface
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class MediaServerUnitTest {
    @Test
    fun testMediaServer() {
        val server = MockMediaServer(1000)
        val response = server.queryMedia("", 0, 0)

        response.subscribeBy(
            onSuccess = {
                it.forEach {
                    println(it)
                }
            },
            onError = {
                it.printStackTrace()
                println(it.localizedMessage)
            }
        )
    }

    class MockMediaServer(val delayTime: Long): MediaServerInterface {
        val gson = GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(Date::class.java, DateDeserializer()).create()!!

        private fun delayBlocking(ms: Long) {
            Observable.just(0).delay(ms, TimeUnit.MILLISECONDS).blockingFirst()
        }

        override fun queryMedia(query: String, page: Int, size: Int): Single<ArrayList<MediaUrl>> {
            return Single.create { emitter ->
                delayBlocking(delayTime)
                val imageResponse = gson.fromJson(jsonImage, GetImageResponse::class.java)
                val videoResponse = gson.fromJson(jsonVideo, GetVideoResponse::class.java)

                val list = arrayListOf<MediaUrl>()
                val medias = arrayListOf<SharedMedia>()

                imageResponse.medias?.also { medias.addAll(it) }
                videoResponse.medias?.also { medias.addAll(it) }

                medias.sortWith(Comparator { e1, e2 -> e1.dateTime.time.compareTo(e2.dateTime.time) })
                medias.forEach { list.add(it.thumbnailUrl()) }

                emitter.onSuccess(list)
            }
        }

        companion object {
            const val jsonImage = "{\n" +
                    "    \"documents\": [\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2018-05-05T09:09:01.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/K0dJ/1546?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 373,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201604/20/starnews/20160420104531451vrlt.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/EQa9bd9jV9t\",\n" +
                    "            \"width\": 560\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2018-05-05T09:09:01.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/K0dJ/1546?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 726,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201604/20/starnews/20160420104531791djzn.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search2.kakaocdn.net/argon/130x130_85_c/3zqxK3tyEP7\",\n" +
                    "            \"width\": 560\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2018-05-05T09:09:01.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/K0dJ/1546?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 709,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201604/20/starnews/20160420104531251wjza.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search2.kakaocdn.net/argon/130x130_85_c/2mpFtu6vNaT\",\n" +
                    "            \"width\": 560\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"news\",\n" +
                    "            \"datetime\": \"2016-02-01T15:04:03.000+09:00\",\n" +
                    "            \"display_sitename\": \"스포츠경향\",\n" +
                    "            \"doc_url\": \"http://v.media.daum.net/v/20160201150403302\",\n" +
                    "            \"height\": 450,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201602/01/sportskhan/20160201150402172mcnb.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search3.kakaocdn.net/argon/130x130_85_c/7I5iJQkLD0Y\",\n" +
                    "            \"width\": 600\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"etc\",\n" +
                    "            \"datetime\": \"2016-01-10T09:15:48.000+09:00\",\n" +
                    "            \"display_sitename\": \"\",\n" +
                    "            \"doc_url\": \"http://www.viva100.com/main/view.php?lcode=&series=&key=20151109001513562\",\n" +
                    "            \"height\": 500,\n" +
                    "            \"image_url\": \"http://www.viva100.com/mnt/images/file/2015y/11m/09d/20151109001513562_1.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search1.kakaocdn.net/argon/130x130_85_c/72AUgSIKf2O\",\n" +
                    "            \"width\": 500\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2018-05-05T09:07:52.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/UYxw/40?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 373,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201604/20/starnews/20160420104531013wklb.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/FSdsR01Ccuj\",\n" +
                    "            \"width\": 560\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"blog\",\n" +
                    "            \"datetime\": \"2015-11-13T20:05:15.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum블로그\",\n" +
                    "            \"doc_url\": \"http://blog.daum.net/pdc0923/10781\",\n" +
                    "            \"height\": 540,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201511/13/10asia/20151113182228858ujph.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search2.kakaocdn.net/argon/130x130_85_c/E5z2FSeS24B\",\n" +
                    "            \"width\": 540\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"blog\",\n" +
                    "            \"datetime\": \"2018-11-29T00:10:00.000+09:00\",\n" +
                    "            \"display_sitename\": \"네이버블로그\",\n" +
                    "            \"doc_url\": \"http://blog.naver.com/youngjae2010/221408386451\",\n" +
                    "            \"height\": 180,\n" +
                    "            \"image_url\": \"http://mblogthumb1.phinf.naver.net/20151129_224/youngjae2010_1448804344950ISmcD_JPEG/%BC%B3%C7%F6.jpg?type=ffn480_320\",\n" +
                    "            \"thumbnail_url\": \"https://search2.kakaocdn.net/argon/130x130_85_c/ECAM2SI2l2p\",\n" +
                    "            \"width\": 321\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2017-11-23T09:51:41.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/pyeongdong./UAYd/1504?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 795,\n" +
                    "            \"image_url\": \"https://t1.daumcdn.net/news/201511/27/seoul/20151127151008032xbrh.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/H18heGSGtht\",\n" +
                    "            \"width\": 499\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2017-11-23T19:09:10.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/UYxw/4?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 873,\n" +
                    "            \"image_url\": \"https://t1.daumcdn.net/news/201602/17/starnews/20160217095712223rxqp.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search1.kakaocdn.net/argon/130x130_85_c/CCkNsylBQtY\",\n" +
                    "            \"width\": 560\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"etc\",\n" +
                    "            \"datetime\": \"2017-09-03T16:09:59.000+09:00\",\n" +
                    "            \"display_sitename\": \"\",\n" +
                    "            \"doc_url\": \"http://www.city.kr/girl/17489991\",\n" +
                    "            \"height\": 333,\n" +
                    "            \"image_url\": \"http://www.city.kr/./files/attach/images/238/991/489/017/a1dbab3867e65fcea56f74aa5c4fea2b.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/EJzZbunuLXZ\",\n" +
                    "            \"width\": 500\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"blog\",\n" +
                    "            \"datetime\": \"2016-11-14T15:12:05.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum블로그\",\n" +
                    "            \"doc_url\": \"http://blog.daum.net/rud091727/166\",\n" +
                    "            \"height\": 756,\n" +
                    "            \"image_url\": \"http://cfile205.uf.daum.net/image/2417A73C582955190C2232\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/KaDRuYCDhFR\",\n" +
                    "            \"width\": 825\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"blog\",\n" +
                    "            \"datetime\": \"2017-02-01T00:08:59.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum블로그\",\n" +
                    "            \"doc_url\": \"http://blog.daum.net/nyky99/27\",\n" +
                    "            \"height\": 977,\n" +
                    "            \"image_url\": \"http://cfile28.uf.tistory.com/image/24351638573ABEC41AFC45\",\n" +
                    "            \"thumbnail_url\": \"https://search1.kakaocdn.net/argon/130x130_85_c/FdfFaurI72S\",\n" +
                    "            \"width\": 550\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"news\",\n" +
                    "            \"datetime\": \"2015-09-12T10:05:04.000+09:00\",\n" +
                    "            \"display_sitename\": \"중앙일보\",\n" +
                    "            \"doc_url\": \"http://v.media.daum.net/v/20150912100504177\",\n" +
                    "            \"height\": 328,\n" +
                    "            \"image_url\": \"http://t1.daumcdn.net/news/201509/12/joongang/20150912100503137dmqm.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/FJC9bD7u2hZ\",\n" +
                    "            \"width\": 500\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"collection\": \"cafe\",\n" +
                    "            \"datetime\": \"2017-11-23T21:28:14.000+09:00\",\n" +
                    "            \"display_sitename\": \"Daum카페\",\n" +
                    "            \"doc_url\": \"http://cafe.daum.net/gjsansang/UYxw/16?q=%22%EC%84%A4%ED%98%84%22&re=1\",\n" +
                    "            \"height\": 813,\n" +
                    "            \"image_url\": \"https://t1.daumcdn.net/news/201511/27/mbn/20151127151837902tdty.jpg\",\n" +
                    "            \"thumbnail_url\": \"https://search4.kakaocdn.net/argon/130x130_85_c/5slPiq7RCvj\",\n" +
                    "            \"width\": 500\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"meta\": {\n" +
                    "        \"is_end\": false,\n" +
                    "        \"pageable_count\": 3835,\n" +
                    "        \"total_count\": 473696\n" +
                    "    }\n" +
                    "}"

            const val jsonVideo = "{\n" +
                    "    \"documents\": [\n" +
                    "        {\n" +
                    "            \"author\": \"채널A\",\n" +
                    "            \"datetime\": \"2018-12-18T12:15:15.000+09:00\",\n" +
                    "            \"play_time\": 281,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/4jy86HYuSWX\",\n" +
                    "            \"title\": \"[핫플]설현, 공연 중 구토하며 실신…팬들 걱정\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/se725kqkmkqkqgtKku8Xguk\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"비몽\",\n" +
                    "            \"datetime\": \"2018-12-15T17:53:04.000+09:00\",\n" +
                    "            \"play_time\": 288,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/8k2DoyJOytt\",\n" +
                    "            \"title\": \"쓰러지기 직전까지 무대한 AOA 설현(SeolHyun) 사뿐사뿐 (Like a Cat) 181215 [포트나이트오픈행사] 4K 직캠 by 비몽\",\n" +
                    "            \"url\": \"http://www.youtube.com/watch?v=nF2UdmYSSCI\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"HBN_\",\n" +
                    "            \"datetime\": \"2018-12-16T10:58:14.000+09:00\",\n" +
                    "            \"play_time\": 60,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/FLQxFabf8Hv\",\n" +
                    "            \"title\": \"AOA(에이오에이) - 사뿐사뿐 설현 감기몸살 헛구역질 실신 화약탓 포트나이트 코리아 오픈 2018\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/vdc54XXMNClCKixKMCxlWNX\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Spinel CAM\",\n" +
                    "            \"datetime\": \"2018-12-15T17:10:09.000+09:00\",\n" +
                    "            \"play_time\": 65,\n" +
                    "            \"thumbnail\": \"https://search1.kakaocdn.net/argon/138x78_80_pr/DldWs4PrBKO\",\n" +
                    "            \"title\": \"181215 공연중에 몸살로 쓰러진 설현 Seolhyun 에이오에이 AOA 4K 60P 직캠 @ 포트나이트 by Spinel\",\n" +
                    "            \"url\": \"http://www.youtube.com/watch?v=806OIiH8Frk\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"HBN_\",\n" +
                    "            \"datetime\": \"2018-12-16T10:58:13.000+09:00\",\n" +
                    "            \"play_time\": 60,\n" +
                    "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/138x78_80_pr/2SV3xdafN3u\",\n" +
                    "            \"title\": \"AOA(에이오에이) - 빙글뱅글 설현 감기몸살 헛구역질 실신 화약탓 포트나이트 코리아 오픈 2018\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/v5d64n63hh2hTuZWUnZ6DZh\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"비몽\",\n" +
                    "            \"datetime\": \"2018-12-15T21:00:39.000+09:00\",\n" +
                    "            \"play_time\": 1001,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/EHoCrh8tlkh\",\n" +
                    "            \"title\": \"실신 직전까지 최선을 다하는 설현 SeolHyun Full ver. AOA (빙글뱅글+익스큐즈미+사뿐사뿐+심쿵해) [포트나이트오픈행사 181215]\",\n" +
                    "            \"url\": \"http://www.youtube.com/watch?v=LSelr-5Ox2w\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"HBN_\",\n" +
                    "            \"datetime\": \"2018-12-16T10:58:12.000+09:00\",\n" +
                    "            \"play_time\": 60,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/4kmYIwrg74v\",\n" +
                    "            \"title\": \"AOA(에이오에이) - EXCUSE ME(익스큐즈미) 설현 감기몸살 헛구역질 실신 화약탓 포트나이트 코리아\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/v705eK77MlHKDAHMB7HsBgs\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"톱데일리\",\n" +
                    "            \"datetime\": \"2018-12-17T09:24:45.000+09:00\",\n" +
                    "            \"play_time\": 70,\n" +
                    "            \"thumbnail\": \"https://search1.kakaocdn.net/argon/138x78_80_pr/D3IfTcAYfyi\",\n" +
                    "            \"title\": \"포트나이트 행사 무대중에 비틀거리며 쓰러지는 AOA 설현 Seolhyun - 톱데일리(Topdaily)\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/vf044QYtgWwQTNYgQNNTrKw\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"OBS\",\n" +
                    "            \"datetime\": \"2018-12-17T15:53:00.000+09:00\",\n" +
                    "            \"play_time\": 98,\n" +
                    "            \"thumbnail\": \"https://search4.kakaocdn.net/argon/138x78_80_pr/2IvQyUKpBVD\",\n" +
                    "            \"title\": \"설현 실신→이홍기 댓글 논란…＂경솔＂VS＂그럴 수 있다＂\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/s31cdkNRhk45Y3d51NDQ4Dk\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"톱데일리\",\n" +
                    "            \"datetime\": \"2018-12-17T09:22:04.000+09:00\",\n" +
                    "            \"play_time\": 224,\n" +
                    "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/138x78_80_pr/2Fm8OHBk6gE\",\n" +
                    "            \"title\": \"쓰러지기 직전 무대 AOA 설현 Seolhyun 사뿐사뿐 새로캠 - 톱데일리(Topdaily)\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/vf3dfAkjI5ttklnSDllHscq\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"톱데일리\",\n" +
                    "            \"datetime\": \"2018-12-17T09:24:10.000+09:00\",\n" +
                    "            \"play_time\": 226,\n" +
                    "            \"thumbnail\": \"https://search4.kakaocdn.net/argon/138x78_80_pr/6Z3Tz4oVHBz\",\n" +
                    "            \"title\": \"181215 포트나이트코리아오픈 AOA 설현 Seolhyun Excuse Me 4K 새로캠 - 톱데일리\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/vb720bXXWrDrwC35XCC5YMX\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"톱데일리\",\n" +
                    "            \"datetime\": \"2018-12-17T09:20:42.000+09:00\",\n" +
                    "            \"play_time\": 221,\n" +
                    "            \"thumbnail\": \"https://search3.kakaocdn.net/argon/138x78_80_pr/EOvRy8UL790\",\n" +
                    "            \"title\": \"181215 포트나이트코리아오픈 AOA 설현 Seolhyun 빙글뱅글 (Bingle Bangle) 새로캠\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/vb8b8TNVHMq1UUKMxUUNLFq\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"HBN_\",\n" +
                    "            \"datetime\": \"2018-12-16T10:58:03.000+09:00\",\n" +
                    "            \"play_time\": 60,\n" +
                    "            \"thumbnail\": \"https://search4.kakaocdn.net/argon/138x78_80_pr/BzgKSOVJkTZ\",\n" +
                    "            \"title\": \"AOA(에이오에이) 감기몸살에도 최선 다한 설현 헛구역질 실신 화약탓 포트나이트 코리아 오픈 2018\",\n" +
                    "            \"url\": \"http://tv.kakao.com/v/v286d8oVhXoiSkWVqiWlioV\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Spinel CAM\",\n" +
                    "            \"datetime\": \"2018-12-15T17:58:04.000+09:00\",\n" +
                    "            \"play_time\": 221,\n" +
                    "            \"thumbnail\": \"https://search4.kakaocdn.net/argon/138x78_80_pr/31fOVeie6Tn\",\n" +
                    "            \"title\": \"81215 몸살때문에 힘들어도 무대 마치는 설현 Seolhyun 에이오에이 AOA 사뿐사뿐 Like a Cat 4K 60P 직캠 @ 포트나이트 by Spinel\",\n" +
                    "            \"url\": \"http://www.youtube.com/watch?v=4l72DYtfmQ4\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Spinel CAM\",\n" +
                    "            \"datetime\": \"2018-12-16T00:31:24.000+09:00\",\n" +
                    "            \"play_time\": 780,\n" +
                    "            \"thumbnail\": \"https://search2.kakaocdn.net/argon/138x78_80_pr/IU92oXIQUAp\",\n" +
                    "            \"title\": \"181215 무대 끝까지 최선을 다하고 쓰러진 설현 SEOLHYUN No Cut AOA 빙글뱅글+Excuse Me+사뿐사뿐 4K 60P 직캠 @ 포트나이트 by Spinel\",\n" +
                    "            \"url\": \"http://www.youtube.com/watch?v=yghc4cslmpc\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"meta\": {\n" +
                    "        \"is_end\": false,\n" +
                    "        \"pageable_count\": 800,\n" +
                    "        \"total_count\": 8539\n" +
                    "    }\n" +
                    "}"
        }
    }
}