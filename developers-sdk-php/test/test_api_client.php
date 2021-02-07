<?php

require_once '../sdk/api_client.php';

use Tebru\Retrofit\Retrofit;
use Tebru\RetrofitHttp\Guzzle6\Guzzle6HttpClient;

use Ryukato\ApiClient;
use Ryukato\AuthorizedClientBuilder;
use Ryukato\ArrayConverterFactory;

$api_secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe";

$retrofit = Retrofit::builder()
    ->setBaseUrl('http://localhost:9999')
    ->setHttpClient(new Guzzle6HttpClient(AuthorizedClientBuilder::build($api_secret)))
    ->addConverterFactory(new ArrayConverterFactory())
    ->build();

$service = $retrofit->create(Ryukato\ApiClient::class);

$body = [
            "ownerAddress" => "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
            "ownerSecret" => "uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=",
            "name" => "NewName"
        ];

$call = $service->update_non_fungible_token('61e14383', '10000001', '00000001', $body);
var_dump($call);

if (false) { // enable this to test actual http request sending 
    $response = $call->execute();

    /*
        Example HTTP Request

        PUT /v1/item-tokens/61e14383/non-fungibles/10000001/00000001 HTTP/1.1
        Host: localhost:9999
        User-Agent: GuzzleHttp/6.5.5 curl/7.64.1 PHP/7.3.24-(to be removed in future macOS)
        content-type: application/json
        Signature: Itb0ySMnx12/2ZUXGQdKJJo7b+hmOtr2I/o0ZuN1kQ5d9JZjpNuNMh4T7t3+UBdgNGAyzTZvVhXJuZ9e/V060w==
        Nonce: BB3NYdAV
        Timestamp: 1612717561481
        Content-Length: 142

        {"ownerAddress":"tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq","ownerSecret":"uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR\/owKInQIYmo=","name":"NewName"}
    */
}


?>
