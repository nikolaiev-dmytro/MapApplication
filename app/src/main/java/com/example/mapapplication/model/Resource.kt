package com.example.mapapplication.model

class Resource<T> {
    var status: Status = Status.LOADING
    var data: T? = null
    var message: String? = null


    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource<T>().apply {
                this.data = data
                this.status = Status.SUCCESS
                this.message = null
            }
        }

        fun <T> error(message: String?, data: T?): Resource<T> {
            return Resource<T>().apply {
                this.message = message
                this.data = data
                this.status = Status.ERROR
            }
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource<T>().apply {
                this.data = data
                this.status = Status.LOADING
                this.message = null
            }
        }
    }


    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}