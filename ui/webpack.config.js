const HtmlWebpackPlugin = require('html-webpack-plugin')
const path = require('path')

module.exports = {
    entry: './src/main.js',
    resolve: {
        alias: {
            svelte: path.resolve('node_modules', 'svelte')
        },
        extensions: ['.mjs', '.ts', '.js', '.json', '.svelte'],
        mainFields: ['svelte', 'module', 'main']
    },
    output: {
        filename: 'main.[hash].js',
        path: path.resolve(__dirname, 'dist')
    },
    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                use: [{ loader: "ts-loader" }],
            },
            {
                test: /\.svelte$/,
                exclude: /node_modules/,
                use: {
                    loader: 'svelte-loader',
                    options: {
                        emitCss: true,
                        preprocess: require('svelte-preprocess')()
                    },
                }
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader',
                ],
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({ template: 'dev/index.html' })
    ],
    devServer: {
        contentBase: path.join(__dirname, 'dev'),
        port: 5000,
        proxy: {
            '/playloggingui': { target: 'http://localhost:9000' }
        }
    }
};
