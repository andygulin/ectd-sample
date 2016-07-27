# etcd

### 安装
#### 安装golang，[golang下载](http://pan.baidu.com/s/1qYgq7Qc)
	cd /data
	tar zxvf go1.6.2.linux-amd64.tar.gz && mv go golang
	vi /etc/profile
	export GOROOT=/data/golang
	export PATH=$GOROOT/bin:$PATH
	source /etc/profile
	
#### 安装etcd
	cd /data
	curl -L https://github.com/coreos/etcd/releases/download/v3.0.3/etcd-v3.0.3-linux-amd64.tar.gz -o etcd-v3.0.3-linux-amd64.tar.gz
	tar xzvf etcd-v3.0.3-linux-amd64.tar.gz && mv etcd-v3.0.3-linux-amd64 etcd && cd etcd
	./etcd --version
	
	vi /etc/profile
	export ETCD_NAME=centos
	export ETCD_DATA_DIR=/data/etcd/data
	export ETCD_LISTEN_PEER_URLS=http://0.0.0.0:2380
	export ETCD_LISTEN_CLIENT_URLS=http://0.0.0.0:2379
	export ETCD_ADVERTISE_CLIENT_URLS=http://0.0.0.0:2379
	source /etc/profile
	
	./etcd &
