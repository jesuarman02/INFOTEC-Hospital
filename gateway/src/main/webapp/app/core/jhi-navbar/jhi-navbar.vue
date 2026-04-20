<template>
  <header>
    <b-navbar data-cy="navbar-gob" type="dark" class="navbar-gob">
      <b-navbar-brand href="https://www.gob.mx" class="brand-gob">
        <img :src="'/content/images/logo_blanco.svg'" alt="Página de inicio, Gobierno de México" class="logo-gob" />
      </b-navbar-brand>
      
      <b-navbar-nav class="ml-auto nav-gob-links flex-row align-items-center">
        <b-nav-item href="https://www.gob.mx/tramites" target="_self" title="Ir a trámites del gobierno">Trámites</b-nav-item>
        <b-nav-item href="https://www.gob.mx/gobierno" target="_self" title="Ir a gobierno">Gobierno</b-nav-item>
        <b-nav-item href="https://www.gob.mx/busqueda" target="_self" title="Haz búsquedas en gobierno">
          <font-awesome-icon icon="search" />
        </b-nav-item>
      </b-navbar-nav>
    </b-navbar>

    <b-navbar data-cy="navbar" toggleable="md" type="light" class="navbar-main bg-white">
      <b-navbar-brand class="brand-main" to="/">
        <img :src="'/content/images/logo_infotec.png'" alt="INFOTEC" class="logo-main" />
      </b-navbar-brand>

      <b-navbar-toggle
        right
        class="jh-navbar-toggler d-lg-none"
        target="header-tabs"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <font-awesome-icon icon="bars" />
      </b-navbar-toggle>

      <b-collapse is-nav id="header-tabs">
        <b-navbar-nav class="ml-auto nav-main-links">
          
          <b-nav-item to="/" exact>
            <span class="nav-content">
              <font-awesome-icon icon="home" />
              <span v-text="t$('global.menu.home')"></span>
            </span>
          </b-nav-item>

          <b-nav-item to="/interfaz-pacientes" active-class="active" v-if="authenticated">
            <span class="nav-content">
              <img src="/content/images/llave.svg" alt="Pacientes" class="icon-custom" />
              <span v-text="t$('global.menu.adminPacientes.main')"></span>
            </span>
          </b-nav-item>

          <b-nav-item-dropdown right id="entity-menu" v-if="hasAnyAuthority('ROLE_ADMIN')" active-class="active" class="pointer" data-cy="entity">
            <template #button-content>
              <span class="nav-content">
                <font-awesome-icon icon="th-list" />
                <span v-text="t$('global.menu.entities.main')"></span>
              </span>
            </template>
            <entities-menu></entities-menu>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown
            right
            id="admin-menu"
            v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated"
            :class="{ 'router-link-active': subIsActive('/admin') }"
            active-class="active"
            class="pointer"
            data-cy="adminMenu"
          >
            <template #button-content>
              <span class="nav-content">
                <font-awesome-icon icon="users-cog" />
                <span v-text="t$('global.menu.admin.main')"></span>
              </span>
            </template>
            <b-dropdown-item to="/admin/gateway" active-class="active">
              <font-awesome-icon icon="road" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.gateway')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/user-management" active-class="active">
              <font-awesome-icon icon="users" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.userManagement')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/metrics" active-class="active">
              <font-awesome-icon icon="tachometer-alt" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.metrics')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/health" active-class="active">
              <font-awesome-icon icon="heart" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.health')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/configuration" active-class="active">
              <font-awesome-icon icon="cogs" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.configuration')"></span>
            </b-dropdown-item>
            <b-dropdown-item to="/admin/logs" active-class="active">
              <font-awesome-icon icon="tasks" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.logs')"></span>
            </b-dropdown-item>
            <b-dropdown-item v-if="openAPIEnabled" to="/admin/docs" active-class="active">
              <font-awesome-icon icon="book" class="dropdown-icon" />
              <span v-text="t$('global.menu.admin.apidocs')"></span>
            </b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown id="languagesnavBarDropdown" right v-if="languages && Object.keys(languages).length > 1">
            <template #button-content>
              <span class="nav-content">
                <font-awesome-icon icon="flag" />
                <span v-text="t$('global.menu.language')"></span>
              </span>
            </template>
            <b-dropdown-item
              v-for="(value, key) in languages"
              :key="`lang-${key}`"
              @click="changeLanguage(key)"
              :class="{ active: isActiveLanguage(key) }"
            >
              {{ value.name }}
            </b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item-dropdown
            right
            id="account-menu"
            :class="{ 'router-link-active': subIsActive('/account') }"
            active-class="active"
            class="pointer"
            data-cy="accountMenu"
          >
            <template #button-content>
              <span class="nav-content">
                <font-awesome-icon icon="user" />
                <span v-text="t$('global.menu.account.main')"></span>
              </span>
            </template>
            <b-dropdown-item data-cy="settings" to="/account/settings" v-if="authenticated" active-class="active">
              <font-awesome-icon icon="wrench" class="dropdown-icon" />
              <span v-text="t$('global.menu.account.settings')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="passwordItem" to="/account/password" v-if="authenticated" active-class="active">
              <font-awesome-icon icon="lock" class="dropdown-icon" />
              <span v-text="t$('global.menu.account.password')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="logout" v-if="authenticated" @click="logout()" id="logout" active-class="active">
              <font-awesome-icon icon="sign-out-alt" class="dropdown-icon" />
              <span v-text="t$('global.menu.account.logout')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="login" v-if="!authenticated" @click="openLogin()" id="login" active-class="active">
              <font-awesome-icon icon="sign-in-alt" class="dropdown-icon" />
              <span v-text="t$('global.menu.account.login')"></span>
            </b-dropdown-item>
            <b-dropdown-item data-cy="register" to="/register" id="register" v-if="!authenticated" active-class="active">
              <font-awesome-icon icon="user-plus" class="dropdown-icon" />
              <span v-text="t$('global.menu.account.register')"></span>
            </b-dropdown-item>
          </b-nav-item-dropdown>

        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </header>
</template>

<script lang="ts" src="./jhi-navbar.component.ts"></script>

<style scoped src="../../../content/css/jhi-navbar.css"></style>