package com.saranggujrati.model

data class BlogData(
    val audio_file: String,
    val author_id: String,
    val author_name: String,
    val banner_image: List<String>,
    val blog_accent_code: String,
    val category_id: String,
    var category_name: String,
    val color: String,
    val content_type: String,
    val create_date: String,
    val created_at: String,
    val created_by: String,
    val deleted_at: String,
    var description: String,
    val id: Int,
    var image: String,
    val is_bookmark: Int,
    val is_editor_picks: String,
    val is_featured: String,
    val is_slider: String,
    val is_vote: Int,
    val is_voting_enable: String,
    val is_weekly_top_picks: String,
    val no_percent: Int,
    val order: String,
    val post_id: String,
    val schedule_date: String,
    val scial_media_image: String,
    val seo_description: String,
    val seo_keyword: String,
    val seo_tag: String,
    val seo_title: String,
    val short_description: String,
    val slug: String,
    val status: String,
    val tags: String,
    val thumb_image: String,
    var time: String,
    var title: String,
    val trimed_description: String,
    val tweet_published: String,
    val updated_at: String,
    var url: String,
    val video_url: String,
    val view_count: Int,
    val voice: String,
    val yes_percent: Int,
    var isBanner:Boolean
) {


    constructor() : this(
        "", "", "", emptyList(), "", "",
        "", "", "", "", "", "", "",
        "", 0, "", 0, "", "", "",
        0, "", "", 0, "", "", "", "",
        "", "", "", "", "", "", "", "",
        "", "", "", "", "", "", "",
        "", 0, "", 0,false
    )


}