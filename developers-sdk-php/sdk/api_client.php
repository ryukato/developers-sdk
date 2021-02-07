<?php

namespace Ryukato;

require_once 'vendor/autoload.php';
require_once 'autosigner.php';
require_once 'array_converter_factory.php';

use GuzzleHttp\Client;
use GuzzleHttp\HandlerStack;
use GuzzleHttp\Handler\CurlHandler;
use Psr\Http\Message\RequestInterface;
use GuzzleHttp\Middleware;

use Tebru\Retrofit\Retrofit;
use Tebru\Retrofit\RetrofitBuilder;
use Tebru\RetrofitHttp\Guzzle6\Guzzle6HttpClient;
use Tebru\Retrofit\Call;

use Tebru\Retrofit\Annotation\GET;
use Tebru\Retrofit\Annotation\POST;
use Tebru\Retrofit\Annotation\PUT;
use Tebru\Retrofit\Annotation\DELETE;
use Tebru\Retrofit\Annotation\Path;
use Tebru\Retrofit\Annotation\Query;
use Tebru\Retrofit\Annotation\Body;

use AuthorizedClient;
use ArrayConverterFactory;

interface ApiClient {
    /**
     * @GET("/v1/time")
     */
    public function time(): Call;

    /**
     * @GET("/v1/services/{service_id}")
     * @Path("service_id")
     */
    public function service_detail(string $service_id): Call;

    /**
     * @GET("/v1/service-tokens/{contract_id}/holders")
     * @Path("contract_id")
     * @Query("limit")
     * @Query("page")
     * @Query("order_by")
     */
    public function service_token_holders(string $contract_id, int $limit, int $page, string $order_by): Call;

    /**
     * @PUT("/v1/service-tokens/{contract_id}")
     * @Path("contract_id")
     * @Body("update_request")
     */
    public function update_service_token_detail(string $contract_id, array $update_request): Call;

    /**
     * @POST("/v1/service-tokens/{contract_id}/mint")
     * @Path("contract_id")
     * @Body("service_token_mint_request")
     */
    public function mint_service_token(string $contract_id, array $service_token_mint_request): Call;

    /**
     * @PUT("/v1/item-tokens/{contract_id}/non-fungibles/{token_type}/{token_index}")
     * @Path("contract_id")
     * @Path("token_type")
     * @Path("token_index")
     * @Body("update_request")
     */
    public function update_non_fungible_token(string $contract_id, string $token_type, string $token_index, array $update_request): Call;
}
?>

