<template>
  <div id="app">
    <ribbon></ribbon>

    <div id="app-header">
      <jhi-navbar></jhi-navbar>
    </div>

    <div class="container-fluid">

      <!-- 🔥 Si NO es calendario -->
      <div v-if="$route.name !== 'Calendario'" class="card jh-card">
        <router-view></router-view>
      </div>

      <!-- 🔥 Si es calendario -->
      <router-view v-else></router-view>

      <b-modal id="login-page" hide-footer lazy hide-header-close>
        <template #modal-title>
          <span data-cy="loginTitle" id="login-title" v-text="t$('login.title')"></span>
        </template>
        <login-form></login-form>
      </b-modal>

      <jhi-footer></jhi-footer>

    </div>
  </div>
</template>

<script lang="ts" src="./app.component.ts"></script>
<style src="../../content/css/login.css"></style>