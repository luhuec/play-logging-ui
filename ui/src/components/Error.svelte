<script lang="ts">
  import { fly } from "svelte/transition";
  import { errorStore } from "../stores";

  let message: string = "";
  let show: boolean = false;
  let timeout = undefined;

  errorStore.subscribe((e) => {
    if (!e) {
      return;
    }

    message = e;
    show = true;

    if (timeout) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(() => (show = false), 2000);
  });
</script>

<style lang="scss">
  .error {
    position: absolute;
    top: 110px;
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;
    width: 400px;
    border-left: 5px solid #e55039;
    background-color: #ffffff;
    padding: 30px 20px;
    border-radius: 5px;
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.16), 0 0 2px 0 rgba(0, 0, 0, 0.12);
  }
</style>

<div>
  {#if show}
    <div transition:fly={{ y: -100 }} class="error">Error: {message}</div>
  {/if}
</div>
