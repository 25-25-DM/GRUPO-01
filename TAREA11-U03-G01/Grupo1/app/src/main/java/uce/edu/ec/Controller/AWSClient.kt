package uce.edu.ec.Controller

import android.util.Log
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSSessionCredentials
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.AmazonClientException

object AWSClient {
    private var ddbClient: AmazonDynamoDBClient? = null

    fun getDynamoDBClient(): AmazonDynamoDBClient {
        if (ddbClient == null) {
            try {
                // Introduce aquí las TRES claves que obtuviste del laboratorio
               
                if (accessKey.isBlank() || secretKey.isBlank() || sessionToken.isBlank()) {
                    throw IllegalStateException("Las credenciales de AWS no pueden estar vacías")
                }

                // Usamos BasicSessionCredentials en lugar de BasicAWSCredentials
                val credentials = BasicSessionCredentials(accessKey, secretKey, sessionToken)

                ddbClient = AmazonDynamoDBClient(credentials).apply {
                    setRegion(Region.getRegion(Regions.US_EAST_1)) // Asegúrate de que sea la región correcta
                }

                Log.d("AWSClient", "Cliente DynamoDB inicializado correctamente")
            } catch (e: Exception) {
                Log.e("AWSClient", "Error al inicializar el cliente DynamoDB", e)
                throw AmazonClientException("Error al inicializar el cliente DynamoDB: ${e.message}")
            }
        }
        return ddbClient!!
    }
}