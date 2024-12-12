package com.rnadigital.monita_android.ecoomerce

import com.rnadigital.monita_android.R

object ProductRepository {
    fun getProducts(): List<Product> {
        return listOf(
            Product(1, "Adidas Shoes", "$99.99", "https://m.media-amazon.com/images/I/611XOzgEUoL._SX695_.jpg"),
            Product(2, "Nike Sneakers", "$129.99", "https://m.media-amazon.com/images/I/61gz3L93EkL._SY695_.jpg"),
            Product(3, "Puma T-Shirt", "$39.99", "https://m.media-amazon.com/images/I/51dAPPq6qnL._SX679_.jpg"),
            Product(4, "Reebok Shorts", "$29.99", "https://m.media-amazon.com/images/I/71HCwjQQG3L._SY741_.jpg"),
            Product(5, "Under Armour Hat", "$19.99", "https://muzikercdn.com/uploads/products/682/68202/thumb_d_gallery_base_f874b8ea.jpg"),
            Product(6, "Columbia Jacket", "$199.99", "https://m.media-amazon.com/images/I/71Nw9XNm0oL._AC_SX679_.jpg"),
            Product(7, "Reebok Shorts", "$29.99", "https://m.media-amazon.com/images/I/71HCwjQQG3L._SY741_.jpg"),
            Product(8, "Under Armour Hat", "$19.99", "https://muzikercdn.com/uploads/products/682/68202/thumb_d_gallery_base_f874b8ea.jpg"),
            Product(9, "Columbia Jacket", "$199.99", "https://m.media-amazon.com/images/I/71Nw9XNm0oL._AC_SX679_.jpg"),
            Product(10, "Adidas Shoes", "$99.99", "https://m.media-amazon.com/images/I/611XOzgEUoL._SX695_.jpg")

            )
    }
}