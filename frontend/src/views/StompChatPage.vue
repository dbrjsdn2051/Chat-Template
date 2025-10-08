<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-center text-h5">
            채팅
          </v-card-title>
          <v-card-text>
            <div class="chat-box">
              <div v-for="(msg, index) in messages" :key="index" :class="['chat-message', msg.senderEmail === senderEmail ? 'chat-message-me' : 'chat-message-other']">
                <strong> {{msg.senderEmail }}: </strong> {{msg.message}}
              </div>
            </div>
            <v-text-field v-model="newMessage" label="메시지 입력" @keyup.enter="sendMessage"/>
            <v-btn color="primary" block @click="sendMessage">전송</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client'

export default {
  data() {
    return {
      messages: [],
      newMessage: '',
      stompClient: null,
      token: '' ,
      senderEmail: null,
    }
  },
  created() {
    this.senderEmail = localStorage.getItem('email');
    this.token = localStorage.getItem('token');
    this.connectWebsocket();
  },
  beforeRouteLeave(to, from, next){
    this.disconnectWebsocket();
  },
  beforeUnmount() {
    this.disconnectWebsocket();
  },
  methods: {
    connectWebsocket() {
      if(this.stompClient && this.stompClient.connected) return;
      const sockJs = new SockJS(`${import.meta.env.VITE_API_BASE_URL}/connect`);
      this.stompClient = Stomp.over(sockJs);
      this.stompClient.connect({
        Authorization: `Bearer ${this.token}`
      }, (frame) => {
        console.log("successfully connected to websocket server")
        this.stompClient.subscribe(`/topic/${1}`, (res) => {
          const parsedMessage = JSON.parse(res.body);
          this.messages.push(parsedMessage);
          this.scrollToBottom();
        })
      })
    },
    sendMessage() {
      if (this.newMessage.trim() === '') return;
      const message = {
        message: this.newMessage,
        senderEmail: this.senderEmail,
      }
      this.stompClient.send(`/publish/${1}`, JSON.stringify(message));
      this.newMessage = '';
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatBox = this.$el.querySelector('.chat-box');
        chatBox.scrollTop = chatBox.scrollHeight;
      })
    },
    disconnectWebsocket() {
      if(this.stompClient && this.stompClient.connected) {
        this.stompClient.unsubscribe(`/topic/${1}`);
        this.stompClient.disconnect();
        console.log("websocket connection closed");
        this.stompClient = null;
      }
    }
  }
}

</script>

<style>
.chat-box {
  height: 300px;
  overflow-y: auto;
  border: 5px solid #ddd;
  margin-bottom: 10px;
}

.chat-message {
  margin-bottom: 10px;
}

.chat-message-me{
  text-align: left;
}

.chat-message-other {
  text-align: right;
}
</style>