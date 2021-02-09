<style lang="scss">
  .curtain {
    position: fixed;
    left: 0;
    right: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    padding-top: 50px;

    .loader,
    .loader:after {
      border-radius: 50%;
      width: 5em;
      height: 5em;
    }

    .loader {
      font-size: 10px;
      border-top: 0.4em solid rgba(200, 200, 200, 0.5);
      border-right: 0.4em solid rgba(200, 200, 200, 0.5);
      border-bottom: 0.4em solid rgba(200, 200, 200, 0.5);
      border-left: 0.4em solid #3498db;
      -webkit-transform: translateZ(0);
      -ms-transform: translateZ(0);
      transform: translateZ(0);
      -webkit-animation: load8 1.1s infinite linear;
      animation: load8 1.1s infinite linear;
    }

    @-webkit-keyframes load8 {
      0% {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
      }
      100% {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }
    @keyframes load8 {
      0% {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
      }
      100% {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
      }
    }
  }
</style>

<div class="curtain">
    <div class="loader"/>
</div>
