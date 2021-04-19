package com.tiff.tiffinbox.Seller.Model

class ViewRecipe {
    var desc: String? = null
    @JvmField
    var imageURL: String? = null
    var price: String? = null

    //    @Exclude
    //    public Map<String, Object> toMap() {
    //        HashMap<String, Object> result = new HashMap<>();
    //        result.put("uid", uid);
    //        result.put("author", author);
    //        result.put("title", title);
    //        result.put("body", body);
    //        result.put("starCount", starCount);
    //        result.put("stars", stars);
    //
    //        return result;
    //    }
    var recipe: String? = null

    constructor() {}
    constructor(desc: String?, imageURL: String?, price: String?) {
        this.desc = desc
        this.imageURL = imageURL
        this.price = price
    }

    constructor(desc: String?, imageURL: String?, price: String?, recipe: String?) {
        this.desc = desc
        this.imageURL = imageURL
        this.price = price
        this.recipe = recipe
    }

//    fun setRecipe(recipe: String?) {
//        field = recipe
//    }
}