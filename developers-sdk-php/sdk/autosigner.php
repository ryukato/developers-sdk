<?php

namespace Ryukato;

require_once 'vendor/autoload.php';
require_once 'signature_generator.php';

use GuzzleHttp\Client;
use GuzzleHttp\HandlerStack;
use GuzzleHttp\Handler\CurlHandler;
use Psr\Http\Message\RequestInterface;
use GuzzleHttp\Middleware;

use Tebru\Retrofit\Retrofit;
use Tebru\Retrofit\RetrofitBuilder;
use Tebru\RetrofitHttp\Guzzle6\Guzzle6HttpClient;
use Tebru\Retrofit\Call;

use SignatureGenerator;
use ReflectionObject;


function get_timestamp()
{
    list($usec, $sec) = explode(" ", microtime());
    return floor( ((float)$usec + (float)$sec) * 1000 );
}

class AuthorizedClientBuilder {
    public static function build(string $api_secret) {
        $handler = new CurlHandler();
        $stack = HandlerStack::create($handler); // Wrap w/ middleware
        $client = new Client(['handler' => $stack]);

        $stack->push(Middleware::mapRequest(function (RequestInterface $r) {
            $timestamp = get_timestamp();

            $nonce_v = array();
            $charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            for ($x = 0; $x < 8; $x ++) {
                array_push($nonce_v, $charset[rand() % strlen($charset)]);
            }

            $nonce = implode('', $nonce_v);

            //
            //  Use some reflection to access private fields
            //
            $reflect = new ReflectionObject($r);
            $v = $reflect->getProperty('method');
            $v->setAccessible(true);

            $http_method = $v->getValue($r);

            $v = $reflect->getProperty('uri');
            $v->setAccessible(true);

            $uri = $v->getValue($r);
            
            $http_path = str_replace("//", "/", $uri->getPath());

            parse_str($uri->getQuery(), $query);

            $s = new SignatureGenerator();
            $signature = $s->generate($api_secret, $http_method, $http_path, 
                                      $timestamp, $nonce, $query, array());

            return $r->withHeader('Signature', $signature)
                     ->withHeader('Nonce', $nonce)
                     ->withHeader('Timestamp', $timestamp);
        }, 'autosigner'));

        return $client;
    }
}

