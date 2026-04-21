import axios from 'axios';
import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import Swal from 'sweetalert2'; // 🔥 AGREGADO
import type AccountService from '../account.service';
import type LoginService from '@/account/login.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const authenticationError: Ref<boolean> = ref(false);
    const login: Ref<string> = ref(null);
    const password: Ref<string> = ref(null);
    const rememberMe: Ref<boolean> = ref(false);
    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const accountService = inject<AccountService>('accountService');
    const loginService = inject<LoginService>('loginService');

    const doLogin = async () => {
      const data = { username: login.value, password: password.value, rememberMe: rememberMe.value };
      try {
        const result = await axios.post('api/authenticate', data);
        const bearerToken = result.headers.authorization;
        if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
          const jwt = bearerToken.slice(7, bearerToken.length);
          if (rememberMe.value) {
            localStorage.setItem('jhi-authenticationToken', jwt);
            sessionStorage.removeItem('jhi-authenticationToken');
          } else {
            sessionStorage.setItem('jhi-authenticationToken', jwt);
            localStorage.removeItem('jhi-authenticationToken');
          }
        }

        authenticationError.value = false;
        
        Swal.fire({
          icon: 'success',
          title: 'Bienvenido',
          html: `<b style="color:#ffb74d">${login.value}</b>`,
          background: 'rgba(20,20,20,0.95)',
          color: '#fff',
          confirmButtonColor: '#ff8c00',
          timer: 2000,
          showConfirmButton: false,
          backdrop: `rgba(0,0,0,0.8)`
        });

        loginService.hideLogin();
        await accountService.retrieveAccount();

        if (route.path === '/forbidden') {
          previousState();
        }
      } catch (_error) {
        authenticationError.value = true;
      }
    };

    return {
      authenticationError,
      login,
      password,
      rememberMe,
      accountService,
      doLogin,
      t$: useI18n().t,
    };
  },
});