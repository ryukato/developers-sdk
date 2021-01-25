<?php

// This module implements generating request signature.
// 
// * API Key: 136db0ad-0fe1-456f-96a4-329be3f93036
// * API Secret: 9256bf8a-2b86-42fe-b3e0-d3079d0141fe
// * Timestamp: 1581850266351
// * Nonce: Bp0IqgXE
// * HTTP method: GET
// * Request path: /v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions
// * Query string: page=2&msgType=coin/MsgSend
// 
// Then flatten body will be
// "Bp0IqgXE1581850266351GET/v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions?page=2&msgType=coin/MsgSend"
// 
// And signature is going to be
// "fasfnqKVVClFam+Dov+YN+rUfOo/PMZfgKx8E36YBtPh7gB2C+YJv4Hxl0Ey3g8lGD0ErEGnD0gqAt85iEhklQ=="
// 
// Author: cocochpie
// Date: 2021/01/23

require_once("request_flattener.php");

class SignatureGenerator {
    // This is to generate signature with flatten request.

    function __createSignTarget($method, $path, $timestamp, $nonce, $parameters) {
        $s = strval($timestamp);
        $signTarget = "{$nonce}{$s}{$method}{$path}";

        if (count($parameters) > 0)
            $signTarget = $signTarget."?";

        return $signTarget;
    }

    public function generate($secret, $method, $path, $timestamp, $nonce, $query_params = array(), $body = array()) {
        //  
        //    Generate signature with given arguments.
        //
        //    Args:
        //        -secret- api-secret
        //        -method- http method
        //        -path- api path
        //        -timestamp- Unix timestamp value
        //        -nonce- random stirng with 8 length
        //        -query_params- query paraemeters
        //        -body- request body
        //
        //    Returns:
        //        -signauture- generated signature
        //        
        //

        $body_flattener = new RequestBodyFlattener();
        $all_parameters = array_replace($query_params, $body);

        $signTarget = $this->__createSignTarget(strtoupper($method), $path, $timestamp, $nonce, $all_parameters);

        if (count($all_parameters) > 0)
            $signTarget = $signTarget.$body_flattener->flatten($all_parameters);

        $raw_hmac = hash_hmac("sha512", utf8_encode($signTarget), utf8_encode($secret), true);
        $result = base64_encode($raw_hmac);

        return $result;
    }
}

?>
