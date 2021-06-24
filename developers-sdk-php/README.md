# LINE Blockchain Developers SDK for PHP
This is a subproject of LINE Blockchain Developers SDK. 
See [README](../README.md) for overview.

## Install PHP composer
```
(From https://getcomposer.org/download/)

php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
php -r "if (hash_file('sha384', 'composer-setup.php') === '756890a4488ce9024fc62c56153228907f1545c228516cbf63f885e036d37e9a59d27d63f46af1d4d07ee0f76181c7d3') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"
php composer-setup.php
php -r "unlink('composer-setup.php');"
```

## Install Dependencies (Retrofit-php)
Please be sure PHP composer is installed

```
cd sdk
../composer.phar install
```

## Run all tests
Run the following command to test the library.

```
cd test
php test_api_client.php
php test.php
```


