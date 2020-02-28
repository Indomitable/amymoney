package eu.vmladenov.amymoney.config

data class AppConfig(
    val dataSource: DataSource
)

abstract class DataSource {
}

data class FileDataSource(val fileUrl: String): DataSource() {

}
