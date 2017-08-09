package remote.poloniex.model

data class ChartDataEntry(
        val date: Long,
        val high: Float,
        val low: Float,
        val open: Float,
        val close: Float,
        val volume: Float,
        val quoteVolume: Float,
        val weightedAverage: Float
)