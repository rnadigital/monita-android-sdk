package digital.rna.monita_android_sdk.monitoringConfig

data class MonitoringConfig(
    val allowManualMonitoring: Boolean,
    val monitoringVersion: String,
    val vendors: List<Vendor>
)