package com.blockdevs.blockdevs

import android.os.AsyncTask
import kotlinx.android.synthetic.main.activity_main.*
import org.kethereum.model.Address
import org.kethereum.rpc.HttpEthereumRPC
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class EthGetBalance(val walletaddress: String?, private var activity: MainActivity?) : AsyncTask<Void, Void, String>() {
    // Initialize wallet balance
    private lateinit var walletbalance: String

    // Infura API
    // Note: Will delete this eventually. Make your own infura.io account if you want.

    // Main net
    //private val web3 = HttpEthereumRPC("https://mainnet.infura.io/v3/a34cf048dac0496da080d1195ec2895c")
    // Ropsten
    //private val web3 = HttpEthereumRPC("https://ropsten.infura.io/v3/a34cf048dac0496da080d1195ec2895c")
    // Rinkeby
    private val rpc = HttpEthereumRPC("https://rinkeby.infura.io/v3/a34cf048dac0496da080d1195ec2895c")


    // Ether value
    private val divider : BigInteger = 1000000000000000000.toBigInteger()


    override fun doInBackground(vararg params: Void?): String? {
        // Convert to Ether
        walletbalance = rpc.getBalance(Address(walletaddress!!))!!.toBigDecimal().divide(divider.toBigDecimal()).toString()

        // return wallet balance
        return walletbalance
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // Run balance on thread
        activity?.txt_wallet2?.text = result
    }



    class SendWallet(val walletaddress: String?, private var activity: MainActivity?) : AsyncTask<Void, Void, String>() {
        // Initialize wallet balance
        private lateinit var walletbalance: String

        // Ether value
        private val divider: BigInteger = 1000000000000000000.toBigInteger()


        override fun doInBackground(vararg params: Void?): String? {
            val mURL = URL("http://192.168.43.194/key") //
            var reqParam =
                URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(walletaddress, "UTF-8")
            with(mURL.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "POST"

                val wr = OutputStreamWriter(getOutputStream());
                wr.write(reqParam)
                wr.flush()

                println("URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    println("Response : $response")
                    return response.toString()
                }
            }
        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Run balance on thread
            activity?.txt_wallet2?.text = result
        }
    }
}
