<?php

require_once('../sdk/signature_generator.php');
require_once('../sdk/request_flattener.php');

{
    $query = [
        'ownerAddress' => 'tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq',
        'ownerSecret' => 'uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=',
        'toAddress' => 'tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp',
        'mintList' => [
            [
                'tokenType' => '10000001',
                'name' => 'NewNFT'
            ],
            [
                'tokenType' => '10000003',
                'name' => 'NewNFT2',
                'meta' => 'New nft 2 meta information'
            ]
        ]
    ];

    $flatten_req_params = "mintList.meta=,New nft 2 meta information&mintList.name=NewNFT,NewNFT2&mintList.tokenType=10000001,10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp";
    "mintList.meta=,New nft 2 meta information&mintList.name=NewNFT,NewNFT2&mintList.tokenType=10000001,10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp";
    "mintList.meta=New nft 2 meta information&mintList.name=NewNFT2&mintList.tokenType=10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp";

    $r = new RequestBodyFlattener();
    $flatten = $r->flatten($query);

    assert($flatten == $flatten_req_params);
}

{
    $method = "GET";
    $path = "/v1/wallets";
    $timestamp = 1581850266351;
    $secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe";
    $nonce = "Bp0IqgXE";

    $s = new SignatureGenerator();
    $x = $s->generate($secret, $method, $path, $timestamp, $nonce);
    assert($x == "2LtyRNI16y/5/RdoTB65sfLkO0OSJ4pCuz2+ar0npkRbk1/dqq1fbt1FZo7fueQl1umKWWlBGu/53KD2cptcCA==");
}

{
    $query = ["page"=>2, "msgType" => "coin/MsgSend"];
    $method = "GET";
    $path = "/v1/wallets/tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq/transactions";
    $timestamp = 1581850266351;
    $secret = "9256bf8a-2b86-42fe-b3e0-d3079d0141fe";
    $nonce = "Bp0IqgXE";

    $s = new SignatureGenerator();
    $x = $s->generate($secret, $method, $path, $timestamp, $nonce, $query);

    assert($x == "5x6bEV1mHkpJpEJMnMsCUH7jV5GzKzA038UwcqpYIAx7Zn1SvA9qhdf+aitu+3juXzXB+qSxM4zRon6/aNVMFg==");
}

{
    // paging parameters sorted by its key when generating signature
    $query = [
      "limit" => 10,
      "page" => 1,
      "orderBy" => "desc"
    ];

    $method = "GET";
    $path = "/v1/service-tokens/a48f097b/holders";
    $timestamp = 1611243023551;
    $secret = "098d8862-477d-49f2-928f-7655489be2d3";
    $nonce = "KScYbbH0";

    $s = new SignatureGenerator();
    $x = $s->generate($secret, $method, $path, $timestamp, $nonce, $query);

    // sign-target will be "KScYbbH01611243023551GET/v1/service-tokens/a48f097b/holders?limit=10&orderBy=desc&page=1"
    assert($x == "8vcqBHXiwGaP5+78ZvuidcoZ/UiKnR1IrgXKzUaRf+HqetD5eHMaeTEW3OvHoKn7Z512WVNuKmRQDW88DvJ1aA==");
}

?>
