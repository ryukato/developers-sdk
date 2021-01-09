from setuptools import find_packages, setup


setup(
    name='link_developers_sdk_py',
    packages=find_packages(include=['sdk']),
    version='0.0.1',
    description='link-developers-sdk for python',
    author='Ryukato',
    license='MIT',
    install_requires=['uplink'],
    setup_requires=['pytest-runner'],
    tests_require=['pytest==4.4.1'],
    test_suite='tests',
)
