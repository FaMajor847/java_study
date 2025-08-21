package mav.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import mav.example.entity.Article;
import mav.example.entity.Author;

public class Main {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static void main(String[] args) throws Exception {
        try {

            entityManagerFactory = Persistence.createEntityManagerFactory("local-jpa-unit");
            entityManager = entityManagerFactory.createEntityManager();

            // Сохранение данных в одной транзакции
            testCascadePersist();

            // Проверка em.clear()
            testEmClear();

            // Проверка ленивой загрузки
            testLazyLoading();

            // Проверка orphanRemoval
            testOrphanRemoval();

            // Проверка каскадного удаления
            testCascadeDelete();

        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    private static void testCascadePersist() {
        System.out.println("===Тест каскадного сохранения===");

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            Author author = new Author("Иван Петров", "ivan@example.com");

            Article article1 = new Article("статья 1");
            Article article2 = new Article("статья 2");
            Article article3 = new Article("статья 3");

            author.addArticle(article1);
            author.addArticle(article2);
            author.addArticle(article3);

            entityManager.persist(author);

            tx.commit();

            System.out.println("Автор и статьи успешно сохранены!");
            System.out.println("ID автора: " + author.getId());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
    // 4. Проверка em.clear()
    private static void testEmClear() {
        System.out.println("\n=== 4. Тест em.clear() ===");

        // Получаем автора (должен быть в кэше)
        Author author1 = entityManager.find(Author.class, 1L);
        System.out.println("Автор из кэша: " + author1.getName());

        // Очищаем контекст
        entityManager.clear();
        System.out.println("Контекст очищен");

        // Получаем заново - должен быть новый запрос к БД
        Author author2 = entityManager.find(Author.class, 1L);
        System.out.println("Автор после очистки: " + author2.getName());

        System.out.println("Объекты разные: " + (author1 != author2));
    }

    private static void testLazyLoading() {
        System.out.println("\n===Тест ленивой загрузки===");

        // Получаем автора без join fetch
        Author author = entityManager.find(Author.class, 1L);
        System.out.println("Автор загружен: " + author.getName());

        // Обращаемся к коллекции - должен выполниться отдельный запрос
        System.out.println("Количество статей: " + author.getArticles().size());

        // Выводим названия статей
        author.getArticles().forEach(article ->
                System.out.println("Статья: " + article.getName()));
    }

    private static void testOrphanRemoval() {
        System.out.println("\n===Тест orphanRemoval===");

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            Author author = entityManager.find(Author.class, 1L);
            System.out.println("Статей до удаления: " + author.getArticles().size());

            // Удаляем первую статью из коллекции
            if (!author.getArticles().isEmpty()) {
                Article articleToRemove = author.getArticles().get(0);
                author.removeArticle(articleToRemove);
                System.out.println("Удаляем статью: " + articleToRemove.getName());
            }

            tx.commit();

            System.out.println("Статья удалена из коллекции, orphanRemoval должен сработать");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Проверка каскадного удаления
    private static void testCascadeDelete() {
        System.out.println("\n===Тест каскадного удаления===");

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            Author author = entityManager.find(Author.class, 1L);
            System.out.println("Удаляем автора: " + author.getName());

            // Удаляем автора - статьи должны удалиться каскадом
            entityManager.remove(author);

            tx.commit();

            System.out.println("Автор и связанные статьи удалены");

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
