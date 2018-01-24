package kz.innopay.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
import java.util.Random;

/**
 * Created by RinRin on 24.01.2018.
 */
public class InnopayPidorBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = "549936602:AAGz5_54YpbBSrypy5NqZy3a2RROeXpFjSY";
    private static final String BOT_USERNAME = "InnopayPidorBot";


    public static void main(String[] args) {
        registerBot();
    }

    //когда бот получает сообщение
    @Override
    public void onUpdateReceived(Update update) {
        //проверим, что сообщение не пустое
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().replace("/", "").equals(Commands.ip_pidor.name())) {
                GetChatAdministrators gca = new GetChatAdministrators();
                gca.setChatId(update.getMessage().getChatId());
                try {
                    List<ChatMember> chatAdmins = getChatAdministrators(gca);

                    Random random = new Random();

                    String currentPidor = chatAdmins.get(random.nextInt(chatAdmins.size()))
                            .getUser().getFirstName();

                    sendMessage(update.getMessage().getChatId(), currentPidor);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    //стандартные команды для регистрации бота
    private static void registerBot() {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new InnopayPidorBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String msg) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try {
            execute(message); //отправим сообщение обратно
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
