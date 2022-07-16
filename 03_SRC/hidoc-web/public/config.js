var configs = [
    {
        host: '*',
        author: '*',
        environment: '默认配置',
        baseServer: 'http://127.0.0.1:24001/',
        imageServer: 'http://127.0.0.1:24001/f/d/u/',
        hdImageFlag: '#hd.image->',
        hdIlinkFlag: '#hd.ilink->',
        networkType: 'inner',
        mastLogin: false
    },
    {
        host: '192.168.14.61:8080',
        author: '*',
        environment: '本地开发配置',
        baseServer: 'http://127.0.0.1:24001/',
        imageServer: 'http://127.0.0.1:24001/f/d/u/',
        hdImageFlag: '#hd.image->',
        hdIlinkFlag: '#hd.ilink->',
        networkType: 'inner',
        mastLogin: false
    },
    {
        host: '192.168.14.155',
        author: '*',
        environment: '公司开发配置',
        baseServer: 'http://127.0.0.1:24001/',
        imageServer: 'http://127.0.0.1:24001/f/d/u/',
        hdImageFlag: '#hd.image->',
        hdIlinkFlag: '#hd.ilink->',
        networkType: 'inner',
        mastLogin: false
    },
    {
        host: '119.12.12.12',
        author: '*',
        environment: '发布公网访问',
        baseServer: 'http://127.0.0.1:24001/',
        imageServer: 'http://127.0.0.1:24001/f/d/u/',
        hdImageFlag: '#hd.image->',
        hdIlinkFlag: '#hd.ilink->',
        networkType: 'outer',
        mastLogin: true
    }
];
