package edu.temple.browsr

import java.io.Serializable

data class Page (var title: String, var url: String) : Serializable {
    constructor() : this("_blank", "")
}